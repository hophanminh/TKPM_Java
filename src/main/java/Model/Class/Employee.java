package Model.Class;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "employee")
public class Employee {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;

    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "name")
    protected String name;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "address")
    protected String address;

    @Column(name = "startDate")
    protected Date startDate;

    @Column(name="salary")
    protected int salary;

    @Column(name = "status")                       // 1: active, 0: nonactive
    protected int status;

    @Column(name = "position")                     // 1: boss, 2: manager, 3: employee
    protected int position;

    @ManyToOne
    @JoinColumn(name = "idStore", nullable = true)
    protected Store store;

    @ManyToOne
    @JoinColumn(name = "idStorage", nullable = true)
    protected Storage storage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
