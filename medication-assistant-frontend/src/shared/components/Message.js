import './Message.css';

export const Message = ({type, children}) => {
  return (
    <p className={`message ${type}`}>
      {children}
    </p>
  );
};