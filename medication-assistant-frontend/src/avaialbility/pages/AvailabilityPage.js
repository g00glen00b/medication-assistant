import {useDecreaseAvailabilityApi, useFindAllAvailabilitiesApi, useIncreaseAvailabilityApi} from '../hooks/apiHooks';
import {Heading, Pagination, Pane} from 'evergreen-ui';
import {AvailabilityTable} from '../components/AvailabilityTable';
import {useState} from 'react';
import {useErrorHandler} from '../../shared/hooks/useErrorHandler';

export const AvailabilityPage = () => {
  const [page, setPage] = useState(1);
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
      <Pagination
        page={page}
        totalPages={availabilitiesResponse?.totalPages || 1}
        onPageChange={setPage} />
    </Pane>
  );
}