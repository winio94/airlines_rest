package com.service.route;

import javax.inject.Inject;
import javax.inject.Named;

import com.domain.Route;

/**
 * Created by Michał on 2016-12-31.
 */
@Named
public class RouteServiceImpl implements RouteService {

    @Inject
    private RouteRepository routeRepository;

    @Override
    public Route create(Route flight) {
        return routeRepository.save(flight);
    }
}