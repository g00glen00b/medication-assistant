import {Medication} from "../../medication/models/medication";

export interface Prescription {
  medication: Medication;
  requiredQuantity: number;
  availableQuantity: number;
  neededQuantity: number;
  initialQuantityperItem: number;
  neededPrescriptions: number;
}