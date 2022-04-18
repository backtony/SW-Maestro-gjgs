import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import {numberJsonType} from '@/utils/Types';
import * as React from 'react';
import {View, ScrollView, StyleSheet} from 'react-native';
import LeftScrollView from './components/leftScrollView/LeftScrollView';
import RightScrollView from './components/rightScrollView/RightScrollView';

interface CategoryViewStates {
  mainCategory: string;
  rightScrollViewRef: ScrollView | null;
  yCord: numberJsonType;
}

class CategoryView extends React.Component<
  React.ReactPropTypes,
  CategoryViewStates
> {
  constructor() {
    super();
    this.state = {mainCategory: '지역별', yCord: {}, rightScrollViewRef: null};
  }

  render() {
    return (
      <View style={this.categoryViewStyle.container}>
        <View style={this.categoryViewStyle.innerContainer}>
          <LeftScrollView
            setMainCategory={(mainCategory: string) => {
              this.setState({mainCategory});
            }}
            rightScrollView={this.state.rightScrollViewRef}
            yCord={this.state.yCord}
            mainCategory={this.state.mainCategory}
          />
          <RightScrollView
            setRef={(ref: ScrollView | null) =>
              this.setState({
                rightScrollViewRef: ref,
              })
            }
            navigation={this.props.navigation}
            yCord={this.state.yCord}
            setYCord={(yCord: numberJsonType) => {
              this.setState({yCord});
            }}
          />
        </View>
      </View>
    );
  }

  private categoryViewStyle = StyleSheet.create({
    container: {
      flex: 1,
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: '#FFF',
    },
    innerContainer: {
      marginTop: STATUSBAR_HEIGHT + 20,
      backgroundColor: '#FFF',
      flex: 1,
      flexDirection: 'row',
      height: '100%',
    },
    leftScrollView: {width: 60, backgroundColor: '#FFF', paddingTop: 10},
  });
}

export default CategoryView;
