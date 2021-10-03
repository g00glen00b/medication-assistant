import {useUpdateAvailabilityApi} from '../hooks/apiHooks';
import {Alert, Modal} from 'antd';
import {AvailabilityForm} from './AvailabilityForm';
import {useEffect, useState} from 'react';
import moment from 'moment';

export const EditAvailabilityDialog = ({availability, onConfirm, onCancel, isShown}) => {
  const formId = 'update-availability-form';
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const initialValues = {
    quantityTypeId: availability?.quantityType?.id,
    quantity: availability?.quantity,
    initialQuantity: availability?.initialQuantity,
    expiryDate: availability == null || availability.expiryDate == null ? null : moment(availability.expiryDate)
  };
  useEffect(() => {
    if (response != null) {
      onConfirm(response);
      setResponse(null);
      setError(null);
    }
  }, [response, onConfirm]);
  const {setRequest} = useUpdateAvailabilityApi(setResponse, setError);

  return (
    <Modal
      visible={isShown}
      title="Edit availability"
      okButtonProps={{form: formId, key: 'submit', htmlType: 'submit'}}
      okText="Update"
      onCancel={onCancel}>
      {error && <Alert
        type="error"
        message={error.message}/>}
      <AvailabilityForm
        formId={formId}
        isMedicationVisible={false}
        initialValues={initialValues}
        onSubmit={body => setRequest({id: availability.id, body})}/>
    </Modal>
  );
};