import {useState} from 'react';

function validate(validations, values) {
  const errors = validations
    .map(validation => validation(values))
    .filter(validation => typeof validation === 'object');
  return {isValid: errors.length === 0, errors: errors.reduce((errors, error) => ({...errors, ...error}), {})};
}

export function useForm(initialState = {}, validations = [], onSubmit = () => {}, onValid = () => {}) {
  const {isValid: initialIsValid, errors: initialErrors} = validate(validations, initialState);
  const [touched, setTouched] = useState({});
  const [values, setValues] = useState(initialState);
  const [errors, setErrors] = useState(initialErrors);
  const [isValid, setValid] = useState(initialIsValid);
  onValid(isValid);

  const changeHandler = ({target: {value, name}}) => {
    const newValues = {...values, [name]: value};
    const {isValid, errors} = validate(validations, newValues);
    setValues(newValues);
    setErrors(errors);
    setValid(isValid);
    setTouched({...touched, [name]: true});
    onValid(isValid);
  };

  const submitHandler = event => {
    event.preventDefault();
    onSubmit(values);
  };

  return {values, errors, touched, isValid, changeHandler, submitHandler};
}

export function isRequired(value) {
  return value != null && value.trim().length > 0;
}

export function isSame(value1, value2) {
  return value1 === value2;
}