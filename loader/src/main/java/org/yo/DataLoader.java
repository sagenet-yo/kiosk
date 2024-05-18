package org.yo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class DataLoader {

    static final String JDBC_CONNECTION_STRING = "jdbc:postgresql://localhost:15432/yo";
    static final String DB_USER_NAME = "yo";
    static final String DB_PASSWORD = "yo";

    public static void main(String[] args) {
        log.info("Hello: {}{}", "Yo", "Han");
        final List<Person> people = getPeople();

        try (final Connection con = DriverManager.getConnection(JDBC_CONNECTION_STRING, DB_USER_NAME, DB_PASSWORD)) {
            for (Person person : people) {
                executeSqls(con, person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void executeSqls(final Connection connection,
                                    final Person person) {
        int personId = createPerson(connection, person);


        final UUID addressId = createAddress(connection, person.getAddress());

        createPersonAddressAssociation(connection, addressId, personId);
    }
    private static void createPersonAddressAssociation(final Connection con, final UUID addressID,
                                                       final int personID)
    {
        final String sql = "insert into address_person_association (address_id, person_id) select ?, ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setObject(1, addressID);
            statement.setInt(2, personID);
            statement.execute();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private static UUID createAddress(final Connection con,
                                      final Address address) {
        final String sql = "insert into address (street, city, state, zip) select ?, ?, ?, ? returning id";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, address.getStreet());
            statement.setString(2, address.getCity());
            statement.setString(3, address.getState());
            statement.setInt(4, address.getZip());
            final ResultSet rs = statement.executeQuery();
            rs.next();
            final UUID id = rs.getObject(1, UUID.class);
//            log.info("address id: {}", id);
            return id;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int createPerson(Connection con, Person person) {
        final String sql = "insert into person (name, age, gender) select ?, ?, ? returning id";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getGender());
            final ResultSet rs = statement.executeQuery();
            rs.next();
            final int id = rs.getInt(1);
//            log.info("address id: {}", id);
            return id;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Person> getPeople() {
        final List<String> firstNames = getLines("/home/yobro/work/kiosk/data/firstnames");

        final List<String> lastNames = getLines("/home/yobro/work/kiosk/data/lastnames");

        final List<String> addresses = getLines("/home/yobro/work/kiosk/data/addresses");

        log.info("Loaded: {} first names, {} last names, {} addresses", firstNames.size(), lastNames.size(), addresses.size());


        final int sizeFirstNames = firstNames.size();
        final int sizeLastNames = lastNames.size();
        final int sizeAddresses = addresses.size();


        List<Person> people = new ArrayList<>();

        for (int i = 0; i < 4000; i++) {
            final String name = String.format("%s %s",
                    StringUtils.capitalize(firstNames.get(getRandomWithin(sizeFirstNames))),
                    StringUtils.capitalize(lastNames.get(getRandomWithin(sizeLastNames)))
            );

            final String addressLine = addresses.get(getRandomWithin(sizeAddresses));

            //777 Brockton Avenue, Abington MA 2351
            String[] parts = StringUtils.split(addressLine, ",");
            final String street = parts[0]; //777 Brockton Avenue
            final String tempCity = parts[1]; // Abington MA 2351

            parts = StringUtils.split(addressLine, " ");
            final String zip = parts[parts.length - 1];
            final String state = parts[parts.length - 2]; //MA

            String city = StringUtils.removeEnd(tempCity, zip);
            city = city.trim();
            city = StringUtils.removeEnd(city, state); //Abington

            final String gender = RandomStringUtils.random(1, 'M', 'F');
            final int age = getRandomWithin(99);

            log.info("Name: {}, Age: {}, Gender: {}, Street: {}, City: {}, State: {}, Zip: {}",
                    name, age, gender, street, city, state, zip);

            final Address address = new Address(street, city, state, Integer.parseInt(zip));
            final Person person = new Person(age, name, gender, address);
            people.add(person);
        }
        return people;
    }

    private static int getRandomWithin(final int limit) {
        return ThreadLocalRandom.current().nextInt(1, limit);
    }

    private static List<String> getLines(final String fileName) {
        try (final InputStream stream = new FileInputStream(fileName)) {
            return IOUtils.readLines(stream, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
