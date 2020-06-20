import datetime
import time
import random

from confluent_kafka import avro
from confluent_kafka.avro import AvroProducer
from faker import Faker

KAFKA_BROKER = "localhost:9092"
SCHEMA_REGISTRY = "http://localhost:8081"
KAFKA_TOPIC = "logs-avro"

value_schema_str = """
{
  "type": "record",
  "namespace": "org.arpit.avro",
  "name": "LogMessageAvro",
  "version": "1",
  "fields": [
    {"name": "ip", "type": "string"},
    {"name": "dt", "type": "string"},
    {"name": "dtEpoch", "type": "long", "logicalType": "timestamp-millis"},
    {"name": "verb", "type": "string"},
    {"name": "resource", "type": "string"},
    {"name": "responseCode", "type": "int"},
    {"name": "referer", "type": "string"},
    {"name": "userAgent", "type": "string"},
    {"name": "responseBytes", "type": "long", "default" : 0}
  ]
}
"""

fake = Faker(locale="en_IN")
value_schema = avro.loads(value_schema_str)
avroProducer = AvroProducer({
    'bootstrap.servers': KAFKA_BROKER,
    'schema.registry.url': SCHEMA_REGISTRY
}, default_value_schema=value_schema)

response = [200, 401, 404, 500, 301]
resources = ["/list", "/wp-content", "/wp-admin", "/explore", "/search/tag/list", "/app/main/posts",
             "/posts/posts/explore", "/apps/cart.jsp?appID="]

start_time = datetime.datetime.now()
while True:
    start_time += datetime.timedelta(milliseconds=random.randint(1000, 10000))
    dt = start_time.strftime('%d/%b/%Y:%H:%M:%S')

    value = {"ip": fake.ipv4(),
             "dt": dt,
             "dtEpoch": 1571463977,
             "verb": fake.http_method(),
             "resource": random.choice(resources),
             "responseCode": random.choice(response),
             "referer": fake.url()[:-1],
             "userAgent": fake.user_agent(),
             "responseBytes": random.randint(1200, 25000000)}
    avroProducer.produce(topic=KAFKA_TOPIC, value=value)
    avroProducer.flush()
    print(f"Message sent : {value}")
    #time.sleep(1)
    break
