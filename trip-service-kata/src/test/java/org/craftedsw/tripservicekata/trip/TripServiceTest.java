package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TripServiceTest {
    private TripService tripService;
    private FakeTripDAO tripDAO;
    private FakeUserSession session;

    @BeforeEach
    public void setup() {
        tripDAO = new FakeTripDAO();
        tripService = new TripService(tripDAO);

        session = new FakeUserSession();
    }

	@Test
    @Order(1)
    public void findTripsWhenUserIsLoggedInAndIsFriend() {
        session.loggedUser = new User();
        User user = new User();
        user.addFriend(session.getLoggedUser());
        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        List<Trip> result = tripService.getTripsByUser(user, session);

        assertEquals(1, result.size());
        assertSame(trip, result.get(0));
        assertSame(user, tripDAO.passedUser);
    }

    @Test
    @Order(2)
    public void NoUserTripsWhenUserIsLoggedInAndIsNotFriend() {
        session.loggedUser = new User();
        User user = new User();
        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        List<Trip> result = tripService.getTripsByUser(user, session);

        assertEquals(0, result.size());
    }

    @Test
    @Order(3)
    public void WhenUserIsNotLoggedInShouldThrowException() {
        User user = new User();
        Trip trip = new Trip();
        tripDAO.foundTrips.add(trip);

        assertThrows(UserNotLoggedInException.class, () -> {
            tripService.getTripsByUser(user, session);
        });
    }
}
