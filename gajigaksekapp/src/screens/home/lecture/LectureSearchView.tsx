import React from 'react';
import {View} from 'react-native';
import LectureSearchViewHeader from './components/lectureSearchView/LectureSearchViewHeader';
import LectureSearchViewPopularKeyword from './components/lectureSearchView/LectureSearchViewPopularKeyword';
import LectureSearchViewRecentlyKeyword from './components/lectureSearchView/LectureSearchViewRecentlyKeyword';

export default class LectureSearchView extends React.Component<
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
        <LectureSearchViewHeader
          navigation={this.props.navigation}
          keyword={this.state.keyword}
          setKeyword={(keyword: string) => this.setState({keyword})}
        />
        <View style={{padding: 20}}>
          <LectureSearchViewPopularKeyword
            setKeyword={(keyword: string) => this.setState({keyword})}
          />
          <LectureSearchViewRecentlyKeyword
            setKeyword={(keyword: string) => this.setState({keyword})}
          />
        </View>
      </View>
    );
  }
}
