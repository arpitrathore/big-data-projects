package org.arpit.scala.spark.rdd00.common

import java.util.concurrent.TimeUnit
import java.util.{Date, Locale}

import com.github.javafaker.Faker

case class Employee(id: Long, firstName: String, lastName: String, gender: String, doj: Date, age: Int, email: String,
                   mobile: String, salary: Double, companyName: String, jobTitle: String, city: String, country: String)

object Employee {

  def buildRandomEmployee: Employee = buildRandomEmployees(1).head

  def buildRandomEmployees(size: Int): Seq[Employee] = {
    val faker = new Faker(new Locale("en", "IND"))
    val employees = (1 to size).map(i => Employee(i.toLong, faker.name.firstName, faker.name.lastName, faker.demographic.sex,
      faker.date.past(3650, TimeUnit.DAYS), faker.number.numberBetween(18, 65), faker.internet.emailAddress,
      faker.phoneNumber.cellPhone, faker.number.randomDouble(2, 60000, 200000),
      faker.company.name.replaceAll(",", ""), faker.job.title, faker.address.city, "India"))
    employees
  }
}
