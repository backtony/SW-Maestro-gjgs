import React from 'react';
import {View} from 'react-native';
import BulletinSearchViewBody from './components/bulletinSearchView/BulletinSearchViewBody';
import BulletinSearchViewHeader from './components/bulletinSearchView/BulletinSearchViewHeader';

export default class BulletinSearchView extends React.Component<
  {},
  {keyword: string}
> {
  constructor(props: any) {
    super(props);
    this.state = {keyword: ''};
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <BulletinSearchViewHeader
          navigation={this.props.navigation}
          keyword={this.state.keyword}
          setKeyword={(text: string) => this.setState({keyword: text})}
          onSubmit={() =>
            this.props.navigation.navigate('bulletinSearchResult', {
              keyword: this.state.keyword,
            })
          }
        />
        <BulletinSearchViewBody />
      </View>
    );
  }
}
