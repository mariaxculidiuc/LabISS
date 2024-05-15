package com.example.proiectiss.service;

import com.example.proiectiss.domain.*;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.repository.IMedicamentComandatRepository;
import com.example.proiectiss.repository.IMedicamentRepository;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.utils.*;
import com.example.proiectiss.utils.Observer;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Service implements com.example.proiectiss.utils.Observable<Event> {

    private IUtilizatorRepository utilizatorRepository;
    private IMedicamentRepository medicamentRepository;
    private IComandaRepository comandaRepository;
    private IMedicamentComandatRepository medicamentComandatRepository;
    private Map<Integer, Boolean> loggedUsers;

    public Service(IUtilizatorRepository utilizatorRepository, IMedicamentRepository medicamentRepository,
                    IComandaRepository comandaRepository, IMedicamentComandatRepository medicamentComandatRepository) {
        this.utilizatorRepository = utilizatorRepository;
        this.medicamentRepository = medicamentRepository;
        this.comandaRepository = comandaRepository;
        this.medicamentComandatRepository = medicamentComandatRepository;
        loggedUsers = new HashMap<>();
    }

    public void addMedicament(Medicament drug){
        medicamentRepository.save(drug);
        notifyObservers(new MedEvent(MedType.ADD, drug));
    }

    public void updateMedicament(Medicament drug){
        Collection<MedicamentComandat> orderItems = medicamentComandatRepository.findAll();
        if (orderItems != null) {
            orderItems.forEach(orderItem -> {
                if(orderItem.getDrug().getID().equals(drug.getID())) {
                    orderItem.setDrug(drug);
                    orderItem.setDrugName(drug.getName());
                    medicamentComandatRepository.update(orderItem);
                }
            });
        }
        medicamentRepository.update(drug);
        notifyObservers(new MedEvent(MedType.UPDATE, drug));
    }

    public void deleteMedicament(Medicament drug){
        Collection<MedicamentComandat> orderItems = medicamentComandatRepository.findAll();
        if (orderItems != null) {
            orderItems.forEach(orderItem -> {
                if(orderItem.getDrug().getID().equals(drug.getID())) {
                    medicamentComandatRepository.delete(orderItem.getID());
                    comandaRepository.delete(orderItem.getOrder().getID());
                }
            });
        }
        medicamentRepository.delete(drug.getID());
        notifyObservers(new MedEvent(MedType.DELETE, drug));
    }


    public Collection<Medicament> findAllMedicamente(){
        Collection<Medicament> drugs = medicamentRepository.findAll();
        return drugs;
    }


    public void signUp(Utilizator user){
        utilizatorRepository.save(user);
    }

    public void logout(Utilizator user){
        if(loggedUsers.containsKey(user.getID())){
            loggedUsers.remove(user.getID());
            return;
        }
        throw new ValidationException("User " + user.getUsername() + " is not login !");
    }

    public Utilizator login(Utilizator user){
        Utilizator findUserSystem = utilizatorRepository.filterByUsernameAndPassword(user);
        if(findUserSystem == null)
            throw new ValidationException("User " + user.getUsername() + " is not register in system !");
        if(loggedUsers.containsKey(findUserSystem.getID()))
            throw new ValidationException("User " + user.getUsername() + " is already login !");
        loggedUsers.put(findUserSystem.getID(), true);
        return findUserSystem;
    }


    private List<Observer<Event>> observersMedsManagementEvent = new ArrayList<>();

    @Override
    public void addObserver(com.example.proiectiss.utils.Observer<Event> observer) {
        observersMedsManagementEvent.add(observer);
    }

    @Override
    public void removeObserver(com.example.proiectiss.utils.Observer<Event> observer) {
        observersMedsManagementEvent.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        observersMedsManagementEvent.stream().forEach(x -> x.update(event));
    }

    public Collection< Pair<Comanda, Collection<MedicamentComandat>> > getAllComenziPeSectie(Utilizator utilizator){
        Collection<Comanda>  comenzi = comandaRepository.findAll();
        Collection<MedicamentComandat> medicamentComandate = medicamentComandatRepository.findAll();
        return  comenzi.stream()
                .filter(order -> utilizator.getID().equals(order.getUser().getID()))
                .map(order -> {
                    Collection<MedicamentComandat> filteredOrderItems = medicamentComandate.stream()
                            .filter(orderItem -> order.getID().equals(orderItem.getOrder().getID()))
                            .collect(Collectors.toList());
                    return new Pair<Comanda, Collection<MedicamentComandat>>(order, filteredOrderItems);
                }).collect(Collectors.toList());
    }

    public void addComanda(Comanda order, Collection<MedicamentComandat> orderItems){
        try{
            // Save the order first
            comandaRepository.save(order);
            // Ensure the order's ID is set
            if (order.getID() == null) {
                throw new Exception("Order ID is null after saving");
            }
            for(MedicamentComandat orderItem: orderItems){
                // Set the order in the orderItem
                orderItem.setOrder(order);
                // Set the ID in the orderItem
                orderItem.setID(new Pair<>(orderItem.getDrug().getID(), orderItem.getOrder().getID()));
                // Save the orderItem
                medicamentComandatRepository.save(orderItem);
            }
            notifyObservers(new ComandaEvent(ComandaEventType.ADD, order));
        } catch (Exception e) {
            System.err.println("Error occurred while adding Comanda: " + e.getMessage());
            e.printStackTrace(); // This will print the stack trace which can help with debugging
        }
    }

    public Collection<Pair<Comanda, Collection<MedicamentComandat>>> filterComandabyStatus(StatusComanda status){
        Collection<Comanda> comenzi = comandaRepository.findAll();
        Collection<MedicamentComandat> medicamentComandats = medicamentComandatRepository.findAll();
        return comenzi.stream()
                .filter(order -> order.getStatus().equals(status))
                .map(order -> {
                    Collection<MedicamentComandat> filteredOrderItems = medicamentComandats.stream()
                            .filter(orderItem -> orderItem.getOrder().getID().equals(order.getID()))
                            .collect(Collectors.toList());
                    return new Pair<Comanda, Collection<MedicamentComandat>>(order, filteredOrderItems);
                }).collect(Collectors.toList());
    }

    public Collection<Comanda> getComandaByStatus(StatusComanda status){
        Collection<Comanda> comenzi = comandaRepository.findAll()
                .stream()
                .filter(order -> order.getStatus().equals(status))
                .collect(Collectors.toList());
        System.out.println(comenzi.size());
        return comenzi;
    }

    public void onoreazaComanda(Comanda order){
        order.setStatus(StatusComanda.ONORATA);
        comandaRepository.update(order);
        notifyObservers(new ComandaEvent(ComandaEventType.HONOR, order));
    }

    public void refuzaComanda(Comanda order){
        order.setStatus(StatusComanda.REFUZATA);
        comandaRepository.update(order);
        notifyObservers(new ComandaEvent(ComandaEventType.REFUSED, order));
    }

    public Collection<MedicamentComandat> getAllMedicamenteComandatePeComanda(Comanda order){
        Collection<MedicamentComandat> medicamentComandats = medicamentComandatRepository.findAll();
        System.out.println(medicamentComandats);
        return medicamentComandats.stream()
                .filter(orderItem -> orderItem.getOrder().getID().equals(order.getID()))
                .collect(Collectors.toList());
    }
}