package agh.ics.oop.model;

public class Genome
{
   private final int[] genome; // since not resizeable we can use a static array
   public Genome(int length)
   {
        genome = new int[length];
        createGenome();
   }

    public Genome(int length, int[] genome1, int energy1, int[] genome2, int energy2)
    {
        genome = new int[length];
        createGenome(genome1, energy1, genome2, energy2);
        mutate();
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

   private void mutate()
   {
        int position = Math.min(genome.length-1,(int)(Math.random() * genome.length));
        genome[position] = (int)Math.round(Math.random() * 7);
   }
}
