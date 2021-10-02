import {useDecreaseAvailabilityApi, useFindAllAvailabilitiesApi, useIncreaseAvailabilityApi} from '../hooks/apiHooks';
import {AvailabilityTable} from '../components/AvailabilityTable';
import {useState} from 'react';
import {useErrorHandler} from '../../shared/hooks/useErrorHandler';
import {Button, Pagination, Typography} from 'antd';
import {CreateAvailabilityDialog} from '../components/CreateAvailabilityDialog';

export const AvailabilityPage = () => {
  const [page, setPage] = useState(1);
  const [refetchCounter, setRefetchCounter] = useState(1);
  const [isCreateDialogShown, setCreateDialogShown] = useState(false);
  const pageSize = 10;
  const [availabilitiesResponse, setAvailabilitiesResponse] = useState({});
  const [editResponse, setEditResponse] = useState(null);
  const {setError} = useErrorHandler();
  useFindAllAvailabilitiesApi(page, pageSize, setAvailabilitiesResponse, setError, refetchCounter);
  const {setId: setIncreaseId} = useIncreaseAvailabilityApi(setEditResponse, setError);
  const {setId: setDecreaseId} = useDecreaseAvailabilityApi(setEditResponse, setError);

  if (editResponse != null) {
    const availabilities = availabilitiesResponse.content
      .map(availability => availability.id === editResponse.id ? editResponse : availability);
    setAvailabilitiesResponse({...availabilitiesResponse, content: availabilities});
    setEditResponse(null);
  }

  return (
    <div>
      <Typography.Title
        level={2}>
        Medication available
      </Typography.Title>
      <AvailabilityTable
        availabilities={availabilitiesResponse?.content || []}
        onIncrease={({id}) => setIncreaseId(id)}
        onDecrease={({id}) => setDecreaseId(id)}/>
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
          total={availabilitiesResponse?.totalElements || 1}
          onChange={setPage} />
      </div>
      <CreateAvailabilityDialog
        isShown={isCreateDialogShown}
        onConfirm={() => {
          setRefetchCounter(refetchCounter + 1);
          setCreateDialogShown(false);
        }}
        onCancel={() => setCreateDialogShown(false)}/>
    </div>
  );
}