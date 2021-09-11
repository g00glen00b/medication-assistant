import {Route, Switch, useRouteMatch} from 'react-router-dom';
import {TopNav} from './TopNav';
import {AvailabilityPage} from '../../avaialbility/pages/AvailabilityPage';
import {Pane} from 'evergreen-ui';

export const ApplicationRouteWrapper = () => {
  const {url} = useRouteMatch();
  return (
    <>
      <TopNav/>
      <Pane
        padding="1em"
        marginTop="53px">
        <Switch>
          <Route path={`${url}/availability`}>
            <AvailabilityPage/>
          </Route>
        </Switch>
      </Pane>
    </>
  );
};