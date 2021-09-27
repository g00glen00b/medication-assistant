import {isRequired, useForm} from '../../shared/hooks/useForm';
// import {Autocomplete, Pane, SelectField, TextInputField} from 'evergreen-ui';
import {useState} from 'react';

function rerenderOnInputValueChange(fn) {
  let oldInputValue = null;
  let oldResult = null;
  return function({inputValue, ...otherProps}) {
    if (inputValue !== oldInputValue) {
      oldInputValue = inputValue;
      oldResult = fn({inputValue, ...otherProps});
    }
    return oldResult;
  };
}

export const AvailabilityForm = ({onSubmit, onValid}) => {
  const initialState = {initialQuantity: 0, quantity: 0, medication: null, quantityType: null};
  const validations = [
    ({initialQuantity}) => initialQuantity != null || {initialQuantity: 'Initial quantity is required'},
    ({initialQuantity}) => initialQuantity >= 0 || {initialQuantity: 'Initial quantity should be positive'},
    ({quantity}) => quantity != null || {quantity: 'Quantity is required'},
    ({quantity, initialQuantity}) => quantity <= initialQuantity || {quantity: 'Quantity should be less than the initial quantity'},
    ({medication}) => isRequired(medication) || {medication: 'Medication is required'},
    ({quantityType}) => isRequired(quantityType) || {quantityType: 'Quantity type is required'}
  ];
  const [medicationInputValue, setMedicationInputValue] = useState('');
  const {errors, touched, values, isValid, changeHandler, submitHandler} = useForm(initialState, validations, onSubmit, onValid);
  return (
    <Pane
      is="form"
      width="100%"
      display="grid"
      gridTemplateColumns="repeat(2, 1fr)"
      gridTemplateRows="repeat(3, auto)">
      <Pane
        gridRow={1}
        gridColumnStart={1}
        gridColumnEnd={3}>
        <Autocomplete
          items={[]}>
          {rerenderOnInputValueChange(({inputValue, getRef, getInputProps}) => {
            setMedicationInputValue(inputValue);
            return (
              <TextInputField
                label="Medication"
                isInvalid={errors.medication != null && touched.medication}
                validationMessage={touched.medication && errors.medication}
                value={inputValue}
                ref={getRef}
                {...getInputProps()}
              />
            )
          })}
        </Autocomplete>
      </Pane>
      <TextInputField
        required
        type="number"
        label="Amount of doses"
        description="The amount of doses left within the medication"
        gridRow={2}
        gridColumn={1}
        paddingRight="1em"
        name="quantity"
        value={values.quantity}
        isInvalid={errors.quantity != null && touched.quantity}
        validationMessage={touched.quantity && errors.quantity}
        onChange={changeHandler}/>
      <TextInputField
        required
        type="number"
        label="Initial amount of doses"
        description="The initial amount of doses that's available within the medication"
        gridRow={2}
        gridColumn={2}
        paddingLeft="1em"
        name="initialQuantity"
        value={values.initialQuantity}
        isInvalid={errors.initialQuantity != null && touched.initialQuantity}
        validationMessage={touched.initialQuantity && errors.initialQuantity}
        onChange={changeHandler}/>
      <SelectField
        required
        label="Dose type"
        description="This can be an amount, a volume in milliliter, ..."
        gridRow={3}
        gridColumnStart={1}
        gridColumnEnd={3}
        name="quantityType"
        isInvalid={errors.quantityType != null && touched.quantityType}
        validationMessage={touched.quantityType && errors.quantityType}
        onChange={changeHandler}/>
    </Pane>
  );
};