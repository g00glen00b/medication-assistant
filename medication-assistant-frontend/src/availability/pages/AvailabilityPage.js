import {
  useDecreaseAvailabilityApi,
  useDeleteAvailabilityApi,
  useFindAllAvailabilitiesApi,
  useIncreaseAvailabilityApi
} from '../hooks/apiHooks';
import {AvailabilityTable} from '../components/AvailabilityTable';
import {useEffect, useState} from 'react';
import {useErrorHandler} from '../../shared/hooks/useErrorHandler';
import {Button, Modal, Pagination, Typography} from 'antd';
import {CreateAvailabilityDialog} from '../components/CreateAvailabilityDialog';
import {useSuccessHandler} from '../../shared/hooks/useSuccessHandler';
import {EditAvailabilityDialog} from '../components/EditAvailabilityDialog';

export const AvailabilityPage = () => {
  const pageSize = 10;
  const [page, setPage] = useState(1);
  const [refetchCounter, setRefetchCounter] = useState(1);
  const [isCreateDialogShown, setCreateDialogShown] = useState(false);
  const [availabilitiesResponse, setAvailabilitiesResponse] = useState({});
  const [updatedAvailability, setUpdatedAvailability] = useState(null);
  const [editResponse, setEditResponse] = useState(null);
  const [confirmDeleteId, setConfirmDeleteId] = useState(null);
  const [deleteResponse, setDeleteResponse] = useState(null);
  const {setError} = useErrorHandler();
  const {setSuccess} = useSuccessHandler();
  useFindAllAvailabilitiesApi(page, pageSize, setAvailabilitiesResponse, setError, refetchCounter);
  const {setId: setDeleteId} = useDeleteAvailabilityApi(setDeleteResponse, setError);
  const {setId: setIncreaseId} = useIncreaseAvailabilityApi(setEditResponse, setError);
  const {setId: setDecreaseId} = useDecreaseAvailabilityApi(setEditResponse, setError);

  if (editResponse != null) {
    const availabilities = availabilitiesResponse.content
      .map(availability => availability.id === editResponse.id ? editResponse : availability);
    setAvailabilitiesResponse({...availabilitiesResponse, content: availabilities});
    setEditResponse(null);
  }
  useEffect(() => {
    if (deleteResponse != null) {
      setConfirmDeleteId(null);
      setRefetchCounter(refetchCounter + 1);
      setSuccess({message: 'Medication successfully deleted'});
      setDeleteResponse(null);
    }
  }, [deleteResponse, refetchCounter, setConfirmDeleteId, setRefetchCounter, setSuccess]);

  return (
    <div>
      <Typography.Title
        level={2}>
        Medication available
      </Typography.Title>
      <AvailabilityTable
        availabilities={availabilitiesResponse?.content || []}
        onIncrease={({id}) => setIncreaseId(id)}
        onDecrease={({id}) => setDecreaseId(id)}
        onEdit={setUpdatedAvailability}
        onDelete={({id}) => setConfirmDeleteId(id)}/>
      <div
        style={{
          display: 'flex',
          flexDirection: 'row',
          marginTop: '1em'
        }}>
        <Button
          type="primary"
          onClick={() => setCreateDialogShown(true)}>
          Add availability
        </Button>
        <Pagination
          current={page}
          style={{
            marginTop: 0,
            marginLeft: 'auto'
          }}
          pageSize={pageSize}
          total={availabilitiesResponse?.totalElements || 1}
          onChange={setPage} />
      </div>
      <CreateAvailabilityDialog
        isShown={isCreateDialogShown}
        onConfirm={() => {
          setRefetchCounter(refetchCounter + 1);
          setCreateDialogShown(false);
          setSuccess({message: 'Medication successfully added'});
        }}
        onCancel={() => setCreateDialogShown(false)}/>
      {updatedAvailability != null && <EditAvailabilityDialog
        availability={updatedAvailability}
        isShown={true}
        onConfirm={() => {
          setRefetchCounter(refetchCounter + 1);
          setUpdatedAvailability(null);
          setSuccess({message: 'Medication successfully updated'});
        }}
        onCancel={() => setUpdatedAvailability(null)}/>}
      <Modal
        visible={confirmDeleteId != null}
        onOk={() => setDeleteId(confirmDeleteId)}
        onCancel={() => setConfirmDeleteId(null)}
        okType="danger"
        okText="Yes"
        cancelText="No">
        Are you sure you want to delete this medication? This cannot be undone.
      </Modal>
    </div>
  );
}