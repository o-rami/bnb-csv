package bnb.data;

import bnb.models.Guest;
import bnb.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(new Host("5fee5bb6-0594-446b-bceb-bceb49241061", "Ramirez", "critFarming@sqen.org",
                "(123) 9876543","28 Bale Avenue", "New York", "NY", 10022,
                new BigDecimal("100.00"), new BigDecimal("150.00")));
        hosts.add(new Host("3a27cd11-b78f-4538-80c1-952d3a6525f2", "Salinger", "jDaBae@finesse.com",
                "(609) 7576171", "123 W 55th St", "Harlem", "NY",
                10031, new BigDecimal("50.00"), new BigDecimal("75.00")));
        hosts.add(new Host("cf6cd63a-028a-4620-9786-60e1d0ce23b7", "Vaisey", "cvaiseyn@ucsd.edu",
                "(208) 9563557", "71 Forest Dale Street", "Boise", "ID",13732,
                new BigDecimal("150.00"), new BigDecimal("200.00")));
    }

    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findById(String GUID) {
        for (Host h : hosts) {
            if (h.getHostId().equals(GUID)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public List<Host> findByLastName(String lastName) {
        List<Host> hostList = new ArrayList<>();
        for (Host h : hosts) {
            if (h.getLastName().equalsIgnoreCase(lastName)) {
                hostList.add(h);
            }
        }
        return hostList;
    }

    @Override
    public Host findByEmail(String email) {
        for (Host h : hosts) {
            if (h.getEmail().equalsIgnoreCase(email)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public List<Host> findByState(String state) {
        List<Host> hostList = new ArrayList<>();
        for (Host h : hosts) {
            if (h.getState().equalsIgnoreCase(state)) {
                hostList.add(h);
            }
        }
        return hostList;
    }

    @Override
    public List<Host> findByCity(String city) {
        List<Host> hostList = new ArrayList<>();
        for (Host h : hosts) {
            if (h.getCity().equalsIgnoreCase(city)) {
                hostList.add(h);
            }
        }
        return hostList;
    }

    @Override
    public List<Host> findByPostalCode(int zip) {
        List<Host> hostList = new ArrayList<>();
        for (Host h : hosts) {
            if (h.getPostalCode() == zip) {
                hostList.add(h);
            }
        }
        return hostList;
    }

}
