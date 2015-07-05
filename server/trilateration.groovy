import io.pivotal.iot.example.Trilateration

levelInDb=payload.get("signal_dbm").doubleValue()
frequency=payload.get("frequencyMhz").doubleValue()

distance=Trilateration.calculateDistanceInMeters(levelInDb,frequency);
payload.put("distance",distance);

return payload
