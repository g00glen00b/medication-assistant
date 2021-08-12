import {GiPill} from 'react-icons/gi';
import './HeroContainer.css';

export const HeroContainer = ({children}) => (
  <div className="hero-container">
    <aside>
      <header>
        <a href="#" className="logo">
          <GiPill/>
          Medication Assistant
        </a>
        <h1>
          You're just a few clicks away from having your own personal medication assistant!
        </h1>
      </header>
    </aside>
    <main>
      {children}
    </main>
  </div>
);