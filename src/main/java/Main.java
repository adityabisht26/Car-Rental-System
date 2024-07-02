import java.util.*;

class Car {
    private final String carId;
    private final String brand;
    private final String model;
    private final double basePricePerDay;
    private boolean isAvailable;

    //Constructor
        public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private final String customerId;
    private final String name;
    private final String phoneNumber;

    public Customer(String customerId, String name, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

class Rental {
    private final Car car;
    private final Customer customer;
    private final int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private final List<Car> cars;
    private final List<Customer> customers;
    private final  List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public Customer findOrCreateCustomer(String name, String phoneNumber) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name) && customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        Customer newCustomer = new Customer("CUS" + (customers.size() + 1), name, phoneNumber);
        addCustomer(newCustomer);
        return newCustomer;
    }

    public Car findCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
            System.out.println("\nCar rented successfully. :)");
        } else {
            System.out.println("Car is not available for rent. :(");
        }
    }

    public void returnCar(Car car, boolean isCarOk, double repairCost) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
            double totalBill = rentalToRemove.getCar().calculatePrice(rentalToRemove.getDays());
            if (!isCarOk) {
                totalBill += repairCost;
                System.out.printf("Car returned successfully with repair cost. Total bill: %.2f rupees%n", totalBill);
            } else {
                System.out.printf("Car returned successfully. Total bill: %.2f rupees%n", totalBill);
            }
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("<<<<<<< Car Rental System >>>>>>>");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your name: ");
                String customerName = scanner.nextLine();
                System.out.print("Enter your phone number: ");
                String phoneNumber = scanner.nextLine();

                if (phoneNumber.length() != 10) {
                    System.out.println("Invalid phone number. It must contain 10 digits. :(");
                    continue;
                }

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine();

                Customer customer = findOrCreateCustomer(customerName, phoneNumber);

                Car selectedCar = findCarById(carId);
                if (selectedCar != null && selectedCar.isAvailable()) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + customer.getCustomerId());
                    System.out.println("Customer Name: " + customer.getName());
                    System.out.println("Phone Number: " + customer.getPhoneNumber());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: %.2f rupees%n", totalPrice);

                    System.out.print("\nConfirm rental (Yes/No): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Yes")) {
                        rentCar(selectedCar, customer, rentalDays);
                    } else {
                        System.out.println("\nRental canceled. :(");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent. :(");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = findCarById(carId);
                if (carToReturn != null && !carToReturn.isAvailable()) {
                    System.out.print("Is the car in good condition (Yes/No)? ");
                    String condition = scanner.nextLine();
                    boolean isCarOk = condition.equalsIgnoreCase("Yes");

                    double repairCost = 0;
                    if (!isCarOk) {
                        System.out.print("Enter the repair cost: ");
                        repairCost = scanner.nextDouble();
                        scanner.nextLine();
                    }
                    returnCar(carToReturn, isCarOk, repairCost);
                } else {
                    System.out.println("Invalid car ID or car is not rented. :(");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option. :(");
            }
        }

        System.out.println("\nThank you for using the Car Rental System. :)");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Fortuner", 3500.0);
        Car car2 = new Car("C002", "Hyundai", "Creta", 2000.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 1500.0);
        Car car4 = new Car("C004", "Hyundai", "Verna", 1600.0);
        Car car5 = new Car("C005", "Skoda", "Slavia", 1700.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.addCar(car4);
        rentalSystem.addCar(car5);

        rentalSystem.menu();
    }
}
