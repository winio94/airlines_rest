package com.service.shortest_path;

public class DistanceCalculator {

    //przybliżona długość promienia Ziemii w kilometrach
    private static final int EARTH_RADIUS = 6371;

    /**
     * Algorytm operaty o Formułę Haversine
     * @param lat1 - szerokość geograficzna punktu startowego
     * @param lat2 - długość geograficzna punktu startowego
     * @param lon1 - szerokość geograficzna punktu końcowego
     * @param lon2 - długość geograficzna punktu końcowego
     * @return odległość między dwoma współrzędnymi
     */
    public long distance(double lat1,
                         double lat2,
                         double lon1,
                         double lon2) {
        double latitudeDistance = Math.toRadians(lat2 - lat1); //
        double longitudeDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c;

        distance = Math.pow(distance, 2);

        return Math.round(Math.sqrt(distance));
    }
}
