package fr.allienna.google.hashcode;

import fr.allienna.google.hashcode.model.*;
import fr.allienna.google.hashcode.repository.DroneRepository;
import fr.allienna.google.hashcode.repository.OrderRepository;
import fr.allienna.google.hashcode.repository.ProductRepository;
import fr.allienna.google.hashcode.repository.WarehouseRepository;
import fr.allienna.google.hashcode.utils.DistanceCalculator;
import fr.allienna.google.hashcode.utils.Populator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;


/**
 * .
 */
public class Simulation {

    private final List<String> commands = new ArrayList<>();
    private int turns;
    private Map map;
    private int deadline;
    private final ProductRepository products = new ProductRepository();
    private final DroneRepository drones = new DroneRepository();
    private final WarehouseRepository warehouses = new WarehouseRepository();
    private final OrderRepository orders = new OrderRepository();

    public static void main(String... args) throws IOException, URISyntaxException {
        List<String> filenames = new ArrayList<>();
        filenames.add("test");
        filenames.add("busy_day");
        filenames.add("redundancy");
        filenames.add("mother_of_all_warehouses");

        for(String filename: filenames) {
            Simulation simulation = new Simulation();
            new Populator(simulation).populate(filename+".in");
            List<String> results = simulation.run();
            simulation.saveSimulation(filename+".out", results);
        }

    }

    private void saveSimulation(String filename, List<String> results) {
        System.out.println("save " + filename + " \n"+ results);
        try {
            String toString = results.stream().reduce((a, b) -> a.concat("\n").concat(b)).get();

            Files.write( Paths.get(this.getClass().getClassLoader().getResource(filename).toURI()),
                    toString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public ProductRepository getProducts() {
        return products;
    }

    public WarehouseRepository getWarehouses() {
        return warehouses;
    }

    public DroneRepository getDrones() {
        return drones;
    }

    public OrderRepository getOrders() {
        return orders;
    }

    public List<String> run() {
        java.util.Map<Order, List<Drone>> managedBy = new HashMap<>();
        for (Order order : orders) {
            for (ProductGroup product : order.getItems()) {
                for (Warehouse warehouse : warehouses) {
                    if (managedBy.get(order) == null || !productAlreadyManaged(product, managedBy.get(order))){
                        if (warehouse.contains(product)) {
                            Drone closestDrone = getClosestCloneFor(warehouse);
                            if (!closestDrone.getPosition().equals(warehouse.getPosition())) {
                                turns -= closestDrone.moveTo(warehouse.getPosition());
                            }
                            if (closestDrone.canPickUpMoreItems()) {
                                turns--;
                                loadMaxProducts(closestDrone, warehouse, product);
                                if (managedBy.get(order) == null) {
                                    List<Drone> managed = new ArrayList<>();
                                    managed.add(closestDrone);
                                    managedBy.put(order, managed);
                                } else {
                                    managedBy.get(order).add(closestDrone);
                                }
                            }
                        }
                    }
                }
            }
            turns -= deliver(order);
        }

        commands.add(0, turns + "");
        return commands;
    }

    private boolean productAlreadyManaged(ProductGroup product, List<Drone> drones) {
        for (Drone drone : drones) {
            for (ProductGroup productGroup : drone.getProducts()) {
                if (product.getProduct().getId() == productGroup.getProduct().getId() && product.getQuantity() == productGroup.getQuantity()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int deliver(Order order) {
        int turns = 0;
        for (Drone drone : getDrones()) {
            if (drone.hasProduct()) {
                turns += drone.moveTo(order.getDestination());
                List<ProductGroup> productGroupList = drone.getProducts();
                for (int i = productGroupList.size() - 1; i >= 0; i--) {
                    ProductGroup product = productGroupList.get(i);
                    order.receive(drone.deliver(product));
                    //System.out.println("Drone [" + drone.getId() + "] Deliver for order [" + order.getId() + "] items of product type ["
                    //        + product.getProduct().getId() + "] [" + product.getQuantity() + "] of them");
                    commands.add(drone.getId() + " D " + order.getId() + " " + product.getProduct().getId() + " " + product.getQuantity());
                    turns++;
                }
            }
        }
        return turns;
    }


    private Drone getClosestCloneFor(Warehouse warehouse) {
        Position warehousePosition = warehouse.getPosition();
        Drone closestDrone = drones.get(0);
        int shortestDistance = 0;
        for (Drone drone : drones) {
            int euclideanDistance = DistanceCalculator.euclideanDistance(warehousePosition, drone.getPosition());
            if (euclideanDistance == 0) {
                return drone;
            }
            if (euclideanDistance < shortestDistance) {
                shortestDistance = euclideanDistance;
                closestDrone = drone;
            }

        }
        return closestDrone;
    }

    private void loadMaxProducts(Drone drone, Warehouse warehouse, ProductGroup product) {
        //System.out.println( "Drone [" + drone.getId() + "] Load at warehouse [" + warehouse.getId() + "] products of product type [" + product.getProduct().getId()
        //                + "], [" + product.getQuantity() + "] of them.");
        commands.add(drone.getId() + " L " + warehouse.getId() + " " + product.getProduct().getId() + " " + product.getQuantity());
        drone.load(warehouse.pick(product));
    }
}

