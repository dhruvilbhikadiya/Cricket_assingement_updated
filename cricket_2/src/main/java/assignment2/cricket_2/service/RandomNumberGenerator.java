package assignment2.cricket_2.service;

import org.springframework.stereotype.Service;

@Service
public class RandomNumberGenerator
{
    public int generateRandomNumber(int range)
    {
        return (int)(Math.random()*(range+1)) ;
    }
}
