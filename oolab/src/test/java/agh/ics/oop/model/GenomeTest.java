package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest
{
    private boolean checkCreatedGenome(Animal animal1, Animal animal2, int[] createdGenome)
    {
        // first possibility
        int[] genome1 = new int[3];

        // w tym przypadku wybierze na poczatku 2 z jednego a potem 2 z drugiego
        for(int i = 0; i<2; ++i)
        {
            genome1[i] = animal2.getGenomeAsIntList()[i];
        }
        genome1[2] = animal1.getGenomeAsIntList()[2];

        // second possibility
        int[] genome2 = new int[3];

        for(int i = 0; i<2; ++i)
        {
            genome2[i] = animal1.getGenomeAsIntList()[i];
        }
        genome2[2] = animal2.getGenomeAsIntList()[2];


        int falseCounter = 0;
        for( int i = 0; i<3; ++i )
        {
            if(genome1[i] != createdGenome[i])
            {
                falseCounter++;
                break;
            }
        }
        for( int i = 0; i<3; ++i )
        {
            if(genome2[i] != createdGenome[i])
            {
                falseCounter++;
                break;
            }
        }
        return falseCounter < 2;
    }

    @Test
    void areTheValuesInGenomesBetweenGivenValuesByTheTask()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 10, 3, 2, 2,0,0, false );

        int[] genomeList = animal1.getGenomeAsIntList();

        for (int i = 0 ; i < genomeList.length; ++i)
        {
            assertTrue( 0 <= genomeList[i] && genomeList[i] <= 7 );
        }
    }

    @Test
    void createDefaultGenomeTest()
    {
        // without mutations
        Animal animal1 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );
        Animal animal2 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        Genome genome = new Genome(animal1.getGenomeAsIntList(),3, animal2.getGenomeAsIntList(),3, 0,0);

        assertTrue(checkCreatedGenome(animal1, animal2, genome.getGenome()));
    }

    @Test
    void checkIfEqualAnimalsWouldCreateTheSameGenome()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        Genome genome = new Genome(animal1.getGenomeAsIntList(), 3, animal1.getGenomeAsIntList(), 3, 0,0);

        assertEquals(animal1.getGenome(), genome);

    }

    // testing Equals method
    @Test
    void checkIfEqualsReturnsTrueForSameGenomes()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        // are fully first list
        Genome genome1 = new Genome(animal1.getGenomeAsIntList(),3, animal1.getGenomeAsIntList(),0, 0,0);
        Genome genome2 = new Genome(animal1.getGenomeAsIntList(),3, animal1.getGenomeAsIntList(),0, 0,0);
        Genome genome3 = new Genome(animal1.getGenomeAsIntList(),3, animal1.getGenomeAsIntList(),0, 0,0);
        // genomes should be the same

        // should be symmetric
        assertTrue(genome1.equals(genome2));
        assertTrue(genome2.equals(genome1));
        // should be transitive
        assertTrue(genome1.equals(genome3));

        // should be reflexive
        assertTrue(genome1.equals(genome1));
    }

    @Test
    void equalsShouldReturnFalseIfGenomesAreDifferent()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 6, 3, 2, 2,0,0, false );
        Animal animal2 = new Animal(new Vector2d(2,2), 6, 3, 2, 2,0,0, false );

        Genome genome1 = animal1.getGenome();
        Genome genome2 = animal2.getGenome();

        boolean accidentallyEqual = true;

        int[] genomeOfAnimal1 = animal1.getGenomeAsIntList();
        int[] genomeOfAnimal2 = animal1.getGenomeAsIntList();

        for (int i = 0; i < genomeOfAnimal1.length; ++i)
        {
            if (genomeOfAnimal1[i] != genomeOfAnimal2[i])
            {
                accidentallyEqual = false;
                break; // mozemy sprawdzac smialo
            }
        }

        assertFalse(genome1.equals(genome2));
        assertFalse(genome2.equals(genome1));
    }

    @Test
    void equalsShouldReturnFalseIfGenomesOfDifferentLengths()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 6, 3, 2, 2,0,0, false );
        Animal animal2 = new Animal(new Vector2d(2,2), 3, 3, 2, 2,0,0, false );

        Genome genome1 = animal1.getGenome();
        Genome genome2 = animal2.getGenome();

        assertFalse(genome1.equals(genome2));
    }

    @Test
    void equalsShouldReturnFalseIfBeingComparedToDifferentType()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 6, 3, 2, 2,0,0, false );
        Genome genome1 = animal1.getGenome();

        assertFalse(genome1.equals(animal1));
    }

    @Test
    void hashCodeShouldBeTheSameForSameGenome()
    {
        Animal animal1 = new Animal(new Vector2d(2,2), 6, 3, 2, 2,0,0, false );
        Genome genome1 = animal1.getGenome();
        Genome genome2 = animal1.getGenome();

        assertEquals(genome1.hashCode(), genome2.hashCode());
    }

    @Test
    void nMutationsShouldChangeExactlyNElements()
    {
        // lets say n = 2
        Animal animal1 = new Animal(new Vector2d(2,2), 6, 3, 2, 0,0,2, false );
        Animal animal2 = new Animal(new Vector2d(2,2), 6, 3, 2, 0,0,2, false );

        Genome genome = new Genome(animal1.getGenomeAsIntList(),3, animal2.getGenomeAsIntList(),3, 2,2);


    }



}