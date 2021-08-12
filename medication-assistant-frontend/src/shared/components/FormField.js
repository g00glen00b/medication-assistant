import './FormField.css';

export const FormField = ({children, className}) => (
  <div className={`form-field ${className}`}>
    {children}
  </div>
);