import React from "react";
import "./HomeView.css";
import HomeDash from "./HomeDash";
import HomeBulletin from "./bulletin/HomeBulletin";
import HomePopup from "./HomePopup";

interface HomeViewStates {
  popup: boolean;
}

export default class HomeView extends React.Component<
  Record<string, never>,
  HomeViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = { popup: false };
  }
  render() {
    return (
      <div className="home-container">
        <HomeDash onClickEdit={() => this.setState({ popup: true })} />
        <HomeBulletin />
        <HomePopup
          on={this.state.popup}
          setVisible={(visible: boolean) => this.setState({ popup: visible })}
        />
      </div>
    );
  }
}
