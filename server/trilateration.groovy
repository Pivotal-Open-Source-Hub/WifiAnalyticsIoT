levelInDb=payload.get("signal_dbm").doubleValue()
freqInMHz=payload.get("frequencyMhz").doubleValue()
			
/**
 * Determining distance from decibel level
 * There’s a useful concept in physics that lets us mathematically relate the signal level in dB to a real-world distance. 
 * Free-space path loss (FSPL) characterizes how the wireless signal degrades over distance in meters (following an inverse square law):
 * FSPL(Db) = 20*log10(d)+ 20*log10(f) + 27.55
 */
double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0
distance = Math.pow(10.0, exp)
payload.put("distance",distance);

return payload
