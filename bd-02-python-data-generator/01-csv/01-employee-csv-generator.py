import csv
import random
from datetime import datetime

from faker import Faker

COUNT = 10000
FILE_PATH = '../../bd-03-test-data/employees.csv'


def generate_csv():
    """
    Creates random employees csv at bd-03-test-data/employees.csv
    """

    fake = Faker(locale='en_IN')

    with open(FILE_PATH, 'w', newline='') as file:
        field_names = ['id', 'firstName', 'lastName', 'gender', 'doj', 'age', 'email', 'mobile', 'salary',
                       'companyName',
                       'jobTitle', 'city', 'country']
        writer = csv.DictWriter(file, fieldnames=field_names)
        writer.writeheader()

        for i in range(1, COUNT):
            full_name = fake.name()
            FLname = full_name.split(" ")
            first_name = FLname[0]
            last_name = FLname[1]

            gender = 'Male' if random.randint(0, 1) == 0 else 'Female'
            doj = fake.date_between_dates(date_start=datetime(2005, 1, 1), date_end=datetime(2020, 1, 1))
            age = random.randint(22, 60)
            email = fake.email()
            mobile = fake.phone_number()
            salary = random.randint(30000, 250000)
            companyName = random.choice(['Infosys', 'TCS', 'Cognizant', 'Wipro', 'Oracle', 'Mindtree'])
            jobTitle = random.choice(['Engineer', 'Sales', 'Associate', 'Manager', 'VP'])
            city = fake.city()
            country = fake.country()

            writer.writerow({'id': i,
                             'firstName': first_name,
                             'lastName': last_name,
                             'gender': gender,
                             'doj': doj,
                             'age': age,
                             'email': email,
                             'mobile': mobile,
                             'salary': salary,
                             'companyName': companyName,
                             'jobTitle': jobTitle,
                             'city': city,
                             'country': country})


if __name__ == '__main__':
    generate_csv()