package fr.allienna.google.hashcode.utils;

import fr.allienna.google.hashcode.model.*;
import fr.allienna.google.hashcode.Simulation;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Read input file and populate simulation
 */
public final class Populator {

    private Simulation simulation;

    public Populator(Simulation simulation) {
        this.simulation = simulation;
    }

    public  void populate(String filename) {
        List<String> lines;
        try {
            lines = readFile(filename);
        } catch (URISyntaxException  | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        initSimulation(lines.get(0));
        simulation.getProducts().addAll(buildProducts(lines.subList(1, 3)));

        int warehouseCount = lineToValues(lines.get(3)).get(0);
        for(int i=4; i<4+2*warehouseCount; i+=2) {
            simulation.getWarehouses().add(buildWarehouse(simulation.getWarehouses().size(),lines.subList(i, i + 2)));
        }

        for(Drone drone: simulation.getDrones()) {
            drone.setPosition(simulation.getWarehouses().get(0).getPosition());
        }

        int orderStartIndex = 4+2*warehouseCount;
        simulation.getOrders().addAll(buildOrders(lines.subList(orderStartIndex, lines.size())));
    }

    private List<String> readFile(String filename) throws URISyntaxException, IOException {
        Path path = Paths.get( this.getClass().getClassLoader().getResource(filename).toURI());
        return Files.readAllLines(path);
    }

    private void initSimulation(String line) {
        List<Integer> parameters = lineToValues(line);
        Map map = new Map(parameters.get(0), parameters.get(1));
        simulation.setMap(map);

        int dronesCount = parameters.get(2);
        int turns = parameters.get(3);
        int maximumLoad = parameters.get(4);
        simulation.setTurns(turns);
        simulation.getDrones().addAll(initializeDrones(dronesCount, maximumLoad));
    }

    private List<Product> buildProducts(List<String> lines) {
        // lines.get(0) contains number of products.
        // we don't need to stock this information
        List<Integer> integers = lineToValues(lines.get(1));
        return IntStream.range(0, integers.size()).mapToObj(i -> new Product(i, integers.get(i))).collect(Collectors.toList());
    }

    private Warehouse buildWarehouse(int id, List<String> lines) {
        List<Integer> coordonnate = lineToValues(lines.get(0));
        Position position = new Position(coordonnate.get(0), coordonnate.get(1));
        List<ProductGroup> stocks = new ArrayList<>();
        List<Integer> stockInformation = lineToValues(lines.get(1));
        for(int i=0; i < stockInformation.size(); i++) {
            Product product = simulation.getProducts().get(i);
            int quantity = stockInformation.get(i);
            stocks.add( new ProductGroup(product, quantity));
        }
        return new Warehouse(id, position, stocks);
    }

    private List<Order> buildOrders(List<String> lines) {
        List<Order> orders = new ArrayList<>();
        int numberOfOrder = lineToValues(lines.get(0)).get(0);
        for(int i=0; i<numberOfOrder; i++) {
            List<Integer> coordonnate = lineToValues(lines.get(i*3+1));
            Position position = new Position(coordonnate.get(0), coordonnate.get(1));
            int numberOfItems = lineToValues(lines.get(i*3+2)).get(0);
            List<ProductGroup> items = new ArrayList<>();
            List<Integer> itemsInformations = lineToValues( lines.get(i*3+3));
            for(int j=0; j<numberOfItems; j++){
                Product product = simulation.getProducts().get(j);
                int quantity = itemsInformations.get(j);
                items.add(new ProductGroup(product, quantity));
            }
            orders.add(new Order(orders.size(), items, position, false));
        }
        return orders;
    }

    private List<Integer> lineToValues(String line) {
        String[] values = line.split(" ");
        return Arrays.stream(values).map(Integer::parseInt).collect(Collectors.toList());
    }

    private List<Drone> initializeDrones(int count, int maximumLoad) {
        List<Drone> drones = new ArrayList<>();
        for( int i=0;i<count; i++) {
            drones.add(new Drone(i, null, maximumLoad, null));
        }
        return drones;
    }
}
