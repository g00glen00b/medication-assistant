export interface CreateAvailabilityRequest {
    medicationName: string;
    quantityTypeId: string;
    quantity: number;
    initialQuantity: number;
    expiryDate: string;
}