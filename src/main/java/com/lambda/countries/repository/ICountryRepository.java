package com.lambda.countries.repository;
// Define a way for our Java Object to interact with our database
import com.lambda.countries.models.Country;
import org.springframework.data.repository.CrudRepository;

// CrudRepository is an Abstract Class that gives us a bunch of useful default goodies that we can use such as get all countries, get country by id etc
// it makes our lives much easier. However we need to tell it what it is working with. We need to provide the class it will work with along with the Primary key datatype

//                                                          Class  PK DatType
//                                                             v       v
public interface ICountryRepository extends CrudRepository<Country, Long>
{
}
