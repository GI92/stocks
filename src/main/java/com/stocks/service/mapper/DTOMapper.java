package com.stocks.service.mapper;

public interface DTOMapper<D, O> {

    O fromDTO(D d);

    D toDTO(O o);
}
