package agh.ics.oop.presenter;

public class Configurations {
    private int heightInput;
    private int widthInput;
    private int howManyPlantsInput;
    private int howManyEnergyFromPlantInput;
    private int howManyPlantsGrowEverydayInput;
    private boolean plantsPreferDeadBodiesCheckbox;
    private int howManyAnimalsOnStartInput;
    private int howManyStartingEnergyAnimalHaveInput;
    private int howLongGenomWillBeInput;
    private boolean ifAnimalsMoveSlowerWhenOlderCheckbox;
    private int energeyNeededToReproduceInput;
    private int energyUsedToReproduceInput;
    private int minNumberOfMutationInput;
    private int maxNumberOfMutationInput;

    public Configurations(
            int heightInput,
            int widthInput,
            int howManyPlantsInput,
            int howManyEnergyFromPlantInput,
            int howManyPlantsGrowEverydayInput,
            boolean plantsPreferDeadBodiesCheckbox,
            int howManyAnimalsOnStartInput,
            int howManyStartingEnergyAnimalHaveInput,
            int howLongGenomWillBeInput,
            boolean ifAnimalsMoveSlowerWhenOlderCheckbox,
            int energeyNeededToReproduceInput,
            int energyUsedToReproduceInput,
            int minNumberOfMutationInput,
            int maxNumberOfMutationInput
    )
    {
        this.heightInput = heightInput;
        this.widthInput = widthInput;
        this.howManyPlantsInput = howManyPlantsInput;
        this.howManyEnergyFromPlantInput = howManyPlantsInput;
        this.howManyPlantsGrowEverydayInput = howManyPlantsGrowEverydayInput;

    }

}
