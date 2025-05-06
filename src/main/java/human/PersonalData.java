package human;

import java.util.Date;

public class PersonalData {
	private String name;
	private String surname;
	private int age;
	private Date birthday;
	private double height;
	private double weight;
	
	public PersonalData(String name, String surname, int age, Date birthday, double height, double weight) {
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.birthday = birthday;
		this.height = height;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public int getAge() {
		return age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public double getHeight() {
		return height;
	}

	public double getWeight() {
		return weight;
	}
	
	
	
}
