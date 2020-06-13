package Class;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table (name = "employee")
public class Employee {
    @Id
    @Column(name = "idEmployee")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int idEmployee;

    @Column(name = "passwordEmployee")
    protected String passwordEmployee;

    @Column(name = "nameEmployee")
    protected String nameEmployee;

    @Column(name = "phoneEmployee")
    protected String phoneEmployee;

    @Column(name = "addressEmployee")
    protected String addressEmployee;

    @Column(name = "startDateEmployee")
    protected Date startDateEmployee;

    @Column(name="salaryEmployee")
    protected int salaryEmployee;

    @Column(name = "statusEmployee")
    protected int statusEmployee;

    @Column(name = "positionEmployee")
    protected int positionEmployee;

    @ManyToOne
    @JoinColumn(name = "idStore", nullable = true)
    protected Store store;

    @ManyToOne
    @JoinColumn(name = "idStorage", nullable = true)
    protected Storage storage;

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getPasswordEmployee() {
        return passwordEmployee;
    }

    public void setPasswordEmployee(String passwordEmployee) {
        this.passwordEmployee = passwordEmployee;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public String getPhoneEmployee() {
        return phoneEmployee;
    }

    public void setPhoneEmployee(String phoneEmployee) {
        this.phoneEmployee = phoneEmployee;
    }

    public String getAddressEmployee() {
        return addressEmployee;
    }

    public void setAddressEmployee(String addressEmployee) {
        this.addressEmployee = addressEmployee;
    }

    public Date getStartDateEmployee() {
        return startDateEmployee;
    }

    public void setStartDateEmployee(Date startDateEmployee) {
        this.startDateEmployee = startDateEmployee;
    }

    public int getSalaryEmployee() {
        return salaryEmployee;
    }

    public void setSalaryEmployee(int salaryEmployee) {
        this.salaryEmployee = salaryEmployee;
    }

    public int getStatusEmployee() {
        return statusEmployee;
    }

    public void setStatusEmployee(int statusEmployee) {
        this.statusEmployee = statusEmployee;
    }

    public int getPositionEmployee() {
        return positionEmployee;
    }

    public void setPositionEmployee(int positionEmployee) {
        this.positionEmployee = positionEmployee;
    }
}
