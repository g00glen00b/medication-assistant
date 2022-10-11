import {QuantityType} from "./quantity-type";

export interface Medication {
    id: string;
    name: string;
    quantityType: QuantityType;
}