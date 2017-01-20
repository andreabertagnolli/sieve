# Sieve
[ ![Codeship Status for andreabertagnolli/sieve](https://app.codeship.com/projects/59094e90-bfa9-0134-8c90-0ea17c17ebe1/status?branch=master)](https://app.codeship.com/projects/196663)

### Validate your java Entities / Value Objects

Sieve is a simple, lightweight validation library, writted in java over the java8's stream specification.

## Where

_WIP_

## How

### Predicate validator
Define your simple `PredicateValidator` on `SomeClass` as:  
```
PredicateValidator.<SomeClass>when(predicate)  
    .returns("ERROR_CODE", "error message");
```

**predicate** is a lambda that implements `Predicate<T>` functional interface, some example could be:  
 - `String::isEmpty` true when the string is empty
 - `Person::isNotOfAge` true when person has less than 18 years  

(obviously you can create all the predicates on your domain objects... let your domain speak!)

### Sieve validator

Handles the more complex objects, you can define it as:

```
SieveValidator.<SomeClass>validator()
    .with(aPredicateOnSomeClass)
    .with(anotherPredicateOnSomeClass)
    .with(SomeClass::getNested, anotherSieveValidatorOnNested)
    .with(SomeClass::getAnotherNested, anotherSieveValidatorOnAnotherNested
    .validate(object)
```

it will run all the predicate validators on "root level" of your object.
If you need to validate some nested objects you can build nested validators and add them specifying the function to get them.

### What does it returns?
The `validate` methods returns a `Stream<Bran>`.  
What is **Bran**?  
Is what you separate from wheat using sieve, so, collecting your stream will return a list of all the brans of the validation:   

```
List<Bran> results = validator.validate(object);
```

### Other cool stuff
Do you know that you can refer to object properties in `PredicateValidator`'s message?  
Like:  
`{{name}} has {{sons.size()}} sons`

excecuted on a Mother called Sara with 3 sons will return:  
``Sara has 3 sons``
