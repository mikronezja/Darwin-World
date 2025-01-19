package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.Objects;
import java.util.Random;

public class Genome
{
   private final int[] genome; // since not resizeable we can use a static array

    private final int minNumberOfMutations;
    private final int maxNumberOfMutations;


   public Genome(int genomLength, int minNumberOfMutations, int maxNumberOfMutations)
   {
       this.minNumberOfMutations = minNumberOfMutations;
       this.maxNumberOfMutations = maxNumberOfMutations;
        genome = new int[genomLength];
        createGenome();
   }

    public Genome(int[] genome1, int energy1, int[] genome2, int energy2, int minNumberOfMutations, int maxNumberOfMutations)
    {
        this.minNumberOfMutations = minNumberOfMutations;
        this.maxNumberOfMutations = maxNumberOfMutations;
        genome = new int[genome1.length];
        createGenome(genome1, energy1, genome2, energy2);
        mutate(mutationNumber());
    }

   public int[] getGenome()
   {
       return genome; // the genome should be available to know which direction the
       // animal is going to move
   }

   private void createGenome(int[] genome1, int energy1, int[] genome2, int energy2)
   {
        float percentageOfParent1 = (float)energy1 / (energy1 + energy2); // checks the percentage based on the animals energy
        int indexToInheritData = 0;
        if (Math.random() < 0.5) // lewa strona jest parenta1
        {
            for (int i = 0; i < percentageOfParent1 * genome.length; indexToInheritData++, i++)
            {
                genome[i] = genome1[i];
            }
            for (int i = indexToInheritData; i < genome.length; indexToInheritData++, i++)
            {
                genome[i] = genome2[i];
            }
        }
        else
        {
            for (int i = 0; i < (1-percentageOfParent1) * genome.length; indexToInheritData++, i++)
            {
                genome[i] = genome2[i];
            }
            for (int i = indexToInheritData; i < genome.length; indexToInheritData++, i++)
            {
                genome[i] = genome1[i];
            }
        }

   }

    private void createGenome()
    {
        for (int i = 0; i < genome.length; i++)
        {
            genome[i] = (int)Math.round(Math.random() * 7); // losuje całkowicie nowy genom
        }
    }

   private void mutate( int mutations )
   {
        int[] indexes = new int[genome.length];
        for (int i = 0; i < genome.length; i++) { indexes[i] = i; } //losuje indeksy od 0 ... genome.length - 1

        for (int i = 0; i < Math.min(mutations,genome.length); i++ )
        {
            int generated_position = (int)(Math.random() * ( genome.length - 1 - i)); // losuje pozycje ktora ma zmienic
            genome[indexes[generated_position]] = (int)Math.round(Math.random() * 7); //  losuje na jaki genom ma byc genom zmieniony

            // swap
            int tempIndex = indexes[indexes.length - 1 - i];
            indexes[indexes.length - 1 - i] = indexes[generated_position];
            indexes[generated_position] = tempIndex;
        }
   }

   private int mutationNumber()
   {
       return (int)Math.floor(Math.random() * (maxNumberOfMutations - minNumberOfMutations + 1) + minNumberOfMutations);
   }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome2 = (Genome) o;
        if (genome.length != genome2.genome.length) return false;

        for (int i = 0; i < genome.length; i++)
        {
            if(genome[i] != genome2.genome[i])
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genome);
    }
}
