package com.example.proiectiss.service;

import com.example.proiectiss.domain.*;
import com.example.proiectiss.repository.IComandaRepository;
import com.example.proiectiss.repository.IMedicamentComandatRepository;
import com.example.proiectiss.repository.IMedicamentRepository;
import com.example.proiectiss.repository.IUtilizatorRepository;
import com.example.proiectiss.utils.*;
import com.example.proiectiss.utils.Observer;

import java.util.*;

public class Service implements com.example.proiectiss.utils.Observable<Event> {

    private IUtilizatorRepository userRepository;
    private IMedicamentRepository drugRepository;
    private IComandaRepository orderRepository;
    private IMedicamentComandatRepository orderItemRepository;
    private Map<Integer, Boolean> loggedUsers;

    public Service(IUtilizatorRepository userRepository, IMedicamentRepository drugRepository,
                    IComandaRepository orderRepository, IMedicamentComandatRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.drugRepository = drugRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        loggedUsers = new HashMap<>();
    }

    public void addMedicament(Medicament drug){
        drugRepository.save(drug);
        notifyObservers(new MedEvent(MedType.ADD, drug));
    }

    public void updateMedicament(Medicament drug){
        Collection<MedicamentComandat> orderItems = orderItemRepository.findAll();
        if (orderItems != null) {
            orderItems.forEach(orderItem -> {
                if(orderItem.getDrug().getID().equals(drug.getID())) {
                    orderItem.setDrug(drug);
                    orderItem.setDrugName(drug.getName());
                    orderItemRepository.update(orderItem);
                }
            });
        }
        drugRepository.update(drug);
        notifyObservers(new MedEvent(MedType.UPDATE, drug));
    }

    public void deleteMedicament(Medicament drug){
        Collection<MedicamentComandat> orderItems = orderItemRepository.findAll();
        if (orderItems != null) {
            orderItems.forEach(orderItem -> {
                if(orderItem.getDrug().getID().equals(drug.getID())) {
                    orderItemRepository.delete(orderItem.getID());
                    orderRepository.delete(orderItem.getOrder().getID());
                }
            });
        }
        drugRepository.delete(drug.getID());
        notifyObservers(new MedEvent(MedType.DELETE, drug));
    }


    public Collection<Medicament> findAllMedicamente(){
        Collection<Medicament> drugs = drugRepository.findAll();
        return drugs;
    }


    public void signUp(Utilizator user){
        userRepository.save(user);
    }

    public void logout(Utilizator user){
        if(loggedUsers.containsKey(user.getID())){
            loggedUsers.remove(user.getID());
            return;
        }
        throw new ValidationException("User " + user.getUsername() + " is not login !");
    }

    public Utilizator login(Utilizator user){
        Utilizator findUserSystem = userRepository.filterByUsernameAndPassword(user);
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
}