package com.company;

/**
 * Created by evgeny on 3/4/17.
 */
public class DomainDouble {
    Double max;
    Double min;

    public DomainDouble(){
        max = 0.0;
        min = 0.0;
    }

    public DomainDouble getPairDomain (DomainDouble domain){
        double min = this.min;
        double max = this.max;
        if (domain.min<this.min)
            min = domain.min;
        if(domain.max > this.max)
            max = domain.max;
        return new DomainDouble(min,max);
    }

    public DomainDouble(Double min , Double max){
        this.max = max;
        this.min = min;
    }
}
