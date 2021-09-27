import {useDecreaseAvailabilityApi, useFindAllAvailabilitiesApi, useIncreaseAvailabilityApi} from '../hooks/apiHooks';
// import {Button, Heading, Pagination, Pane} from 'evergreen-ui';
import {AvailabilityTable} from '../components/AvailabilityTable';
import {useState} from 'react';
import {useErrorHandler} from '../../shared/hooks/useErrorHandler';
import {CreateAvailabilityDialog} from '../components/CreateAvailabilityDialog';

export const AvailabilityPage = () => {
  const [page, setPage] = useState(1);
  const [isCreateDialogShown, setCreateDialogShown] = useState(false);
  const pageSize = 10;
  const [availabilitiesResponse, setAvailabilitiesResponse] = useState({});
  const [editResponse, setEditResponse] = useState(null);
  const {setError} = useErrorHandler();
  useFindAllAvailabilitiesApi(page, pageSize, setAvailabilitiesResponse, setError);
  const {setId: setIncreaseId} = useIncreaseAvailabilityApi(setEditResponse, setError);
  const {setId: setDecreaseId} = useDecreaseAvailabilityApi(setEditResponse, setError);

  if (editResponse != null) {
    const availabilities = availabilitiesResponse.content
      .map(availability => availability.id === editResponse.id ? editResponse : availability);
    setAvailabilitiesResponse({...availabilitiesResponse, content: availabilities});
    setEditResponse(null);
  }
  return (
    <Pane>
      <Heading
        is="h1"
        marginBottom="1em"
        marginTop="1em"
        fontSize="2em"
        fontWeight={400}>
        Medication available
      </Heading>
      <AvailabilityTable
        availabilities={availabilitiesResponse?.content || []}
        onIncrease={({id}) => setIncreaseId(id)}
        onDecrease={({id}) => setDecreaseId(id)}/>
      <Pane
        display="flex"
        flexDirection="row">
        <Button
          appearance="primary"
          marginTop="1.3333333em"
          onClick={() => setCreateDialogShown(true)}>
          Add availability
        </Button>
        <Pagination
          page={page}
          marginTop={0}
          marginLeft="auto"
          totalPages={availabilitiesResponse?.totalPages || 1}
          onPageChange={setPage} />
      </Pane>
      <CreateAvailabilityDialog
        isShown={isCreateDialogShown}
        onConfirm={() => console.log('confirm')}
        onCloseComplete={argument => {
          console.log('closecomplete');
          setCreateDialogShown(false);
        }}/>
    </Pane>
  );
}