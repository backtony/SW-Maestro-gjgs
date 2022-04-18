import React from 'react';
import {View, ScrollView, StyleSheet} from 'react-native';
import Category from '../../../utils/Category';
import MatchingCategoryViewHeader from './components/matchingCategoryView/MatchingCategoryViewHeader';
import MatchingCategoryViewBlock from './components/matchingCategoryView/MatchingCategoryViewBlock';

interface MatchingCategoryViewStates {
  selectedMainCategory: string;
}

export default class MatchingCategoryView extends React.Component<
  {},
  MatchingCategoryViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {selectedMainCategory: ''};
  }
  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <MatchingCategoryViewHeader
          navigation={this.props.navigation}
          selectedMainCategory={this.state.selectedMainCategory}
          subZone={this.props.route.params.subZone}
        />
        <ScrollView
          style={this.styles.scrollView}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}>
          <View style={this.styles.innerModalConatiner}>
            {Category.getCategoryList().map(main => (
              <MatchingCategoryViewBlock
                mainCategory={main}
                selectedMainCategory={this.state.selectedMainCategory}
                setMainCategory={(main: string) =>
                  this.setState({selectedMainCategory: main})
                }
              />
            ))}
          </View>
        </ScrollView>
      </View>
    );
  }
  private styles = StyleSheet.create({
    innerModalConatiner: {
      flexDirection: 'row',
      marginTop: 20,
      flexWrap: 'wrap',
      justifyContent: 'space-between',
      flex: 1,
    },
    scrollView: {
      paddingHorizontal: 20,
      flex: 1,
    },
  });
}
