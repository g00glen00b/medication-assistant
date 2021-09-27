// import {Dialog} from 'evergreen-ui';
import {AvailabilityForm} from './AvailabilityForm';
import {useState} from 'react';

export const CreateAvailabilityDialog = ({isShown, onConfirm, onCancel}) => {
  const [isValid, setValid] = useState(false);
  return (
    <Dialog
      isShown={isShown}
      title="Create availability"
      onConfirm={onConfirm}
      onCloseComplete={onCancel}
      isConfirmDisabled={!isValid}>
      <AvailabilityForm
        onValid={setValid}/>
    </Dialog>
  );
}