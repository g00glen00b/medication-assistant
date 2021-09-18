import {useFindAllAvailabilitiesApi} from '../hooks/apiHooks';
import {Heading, Pane} from 'evergreen-ui';
import {AvailabilityTable} from '../components/AvailabilityTable';

export const AvailabilityPage = () => {
  const {response} = useFindAllAvailabilitiesApi(1, 10);
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
      {response && response?.data && <AvailabilityTable availabilities={response.data.content} />}
    </Pane>
  );
}