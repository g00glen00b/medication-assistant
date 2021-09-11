// Inspection disabled to prevent warnings for color attributes on top of Evergreen
// noinspection HtmlUnknownTarget

import {GiPill} from 'react-icons/gi';
import {Link, Pane} from 'evergreen-ui';
import {Link as RouterLink} from 'react-router-dom';

export const TopNav = () => (
  <Pane
    is="nav"
    padding="1em"
    display="flex"
    flexDirection="row"
    alignItems="center"
    borderBottom="muted"
    position="fixed"
    top={0}
    background="white"
    width="100%">
    <Link
      paddingRight="3em"
      display="flex"
      flexDirection="row"
      alignItems="center"
      is={RouterLink}
      to="/app/home">
      <GiPill
        style={{marginRight: '0.5em'}}/>
      Medication assistant
    </Link>
    <Link
      paddingX="1em"
      is={RouterLink}
      to="/app/availabilities">
      Availabilities
    </Link>
    <Link
      paddingX="1em"
      is={RouterLink}
      to="/app/schedule">
      Schedule
    </Link>
  </Pane>
);