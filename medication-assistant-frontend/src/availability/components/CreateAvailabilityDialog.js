import {useEffect, useState} from 'react';
import {Alert, Modal} from 'antd';
import {AvailabilityForm} from './AvailabilityForm';
import {useCreateAvailabilityApi} from '../hooks/apiHooks';

export const CreateAvailabilityDialog = ({isShown, onConfirm, onCancel}) => {
  const formId = 'create-availability-form';
  const [availability, setAvailability] = useState(null);
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  useEffect(() => {
    if (response != null) {
      onConfirm(response);
      setResponse(null);
      setError(null);
    }
  }, [response, onConfirm]);
  useCreateAvailabilityApi(availability, setResponse, setError);

  return (
    <Modal
      visible={isShown}
      title="Create availability"
      okButtonProps={{form: formId, key: 'submit', htmlType: 'submit'}}
      okText="Create"
      onCancel={onCancel}>
      {error && <Alert
        type="error"
        message={error.message}/>}
      <AvailabilityForm
        formId={formId}
        onSubmit={setAvailability}/>
    </Modal>
  );
}