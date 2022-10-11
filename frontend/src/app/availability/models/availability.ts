import {Medication} from "../../medication/models/medication";

export interface Availability {
    id: string;
    medication: Medication;
    quantity: number;
    initialQuantity: number;
    expiryDate: string;
}