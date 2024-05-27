package bnb.data;

import bnb.models.Host;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {
    private final String filePath;
    public HostFileRepository(@Value("${hostFilePath}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() {
        List<Host> all = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    all.add(deserialize(fields));
                }
            }
        } catch (FileNotFoundException ex) {
            // don't throw on read
        } catch (IOException ex) {
            //
        }
        return all;
    }

    public Host findById(String GUID) {
        List<Host> hostList = findAll();
        for (Host h : hostList) {
            if (h.getHostId().equalsIgnoreCase(GUID)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public List<Host> findByLastName(String lastName) {
        List<Host> byName = new ArrayList<>();
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getLastName().equalsIgnoreCase(lastName)) {
                byName.add(host);
            }
        }
        return byName;
    }

    @Override
    public Host findByEmail(String email) {
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getEmail().equalsIgnoreCase(email)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public List<Host> findByState(String state) {
        List<Host> byState = new ArrayList<>();
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getState().equalsIgnoreCase(state)) {
                byState.add(host);
            }
        }
        return byState;
    }

    @Override
    public List<Host> findByCity(String city) {
        List<Host> byCity = new ArrayList<>();
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getCity().equalsIgnoreCase(city)) {
                byCity.add(host);
            }
        }
        return byCity;
    }

    @Override
    public List<Host> findByPostalCode(int zip) {
        List<Host> byCity = new ArrayList<>();
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getPostalCode() == zip) {
                byCity.add(host);
            }
        }
        return byCity;
    }

    private Host deserialize(String[] fields) {
        Host newHost = new Host();
        newHost.setHostId(fields[0]);
        newHost.setLastName(fields[1]);
        newHost.setEmail(fields[2]);
        newHost.setPhoneNumber(fields[3]);
        newHost.setAddress(fields[4]);
        newHost.setCity(fields[5]);
        newHost.setState(fields[6]);
        newHost.setPostalCode(Integer.parseInt(fields[7]));
        newHost.setStandardRate(new BigDecimal(fields[8]));
        newHost.setWeekendRate(new BigDecimal(fields[9]));
        return newHost;
    }

}
