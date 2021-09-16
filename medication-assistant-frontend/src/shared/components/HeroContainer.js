// Inspection disabled to prevent warnings for color attributes on top of Evergreen
// noinspection HtmlUnknownTarget

import {GiPill} from 'react-icons/gi';
import {Heading, Pane} from 'evergreen-ui';

export const HeroContainer = ({children}) => (
  <Pane
    display="flex"
    height="100vh"
    width="100%"
    >
    <Pane
      is="aside"
      background="blue400"
      maxWidth="35em"
      padding="4em">
      <Pane
        is="header">
        <Heading
          is="h1"
          color="white"
          to="/app/home"
          size={700}
          display="flex"
          alignItems="center"
          marginBottom="2em">
          <GiPill
            style={{marginRight: '0.5em'}}/>
          Medication Assistant
        </Heading>
        <Heading
          is="h2"
          color="white"
          size={600}>
          You're just a few clicks away from having your own personal medication assistant!
        </Heading>
      </Pane>
    </Pane>
    <Pane
      is="main"
      display="flex"
      flexDirection="column"
      flex={1}
      padding="4em"
      elevation={4}>
      {children}
    </Pane>
  </Pane>
);