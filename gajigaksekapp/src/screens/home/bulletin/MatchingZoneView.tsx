import React from 'react';
import {View, ScrollView, StyleSheet, Text} from 'react-native';
import Zone from '../../../utils/Zone';
import MatchingZoneViewHeader from './components/matchingZoneView/MatchingZoneViewHeader';
import MatchingZoneViewMainZone from './components/matchingZoneView/MatchingZoneViewMainZone';
import MatchingZoneViewSubZone from './components/matchingZoneView/MatchingZoneViewSubZone';
import MatchingZoneViewSmallZone from './components/matchingZoneView/MatchingZoneViewSmallZone';

const mainCategoryList = ['서울', '경기', '인천'];

interface MatchingZoneViewStates {
  selectedMainZone: string;
  selectedSubZone: string;
}

export default class MatchingZoneView extends React.Component<
  {},
  MatchingZoneViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {selectedMainZone: '서울', selectedSubZone: ''};
  }
  render() {
    return (
      <View style={this.styles.container}>
        <MatchingZoneViewHeader
          navigation={this.props.navigation}
          selectedSubZone={this.state.selectedSubZone}
        />

        <View style={{paddingHorizontal: 20, flex: 1}}>
          <View style={this.styles.innerModalConatiner}>
            <View style={this.styles.mainZoneWrapper}>
              {mainCategoryList.map((main: string) => (
                <MatchingZoneViewMainZone
                  selectedMainZone={this.state.selectedMainZone}
                  mainZone={main}
                  setMainZone={(main: string) =>
                    this.setState({selectedMainZone: main})
                  }
                />
              ))}
            </View>
            <ScrollView
              style={{marginLeft: 20, marginBottom: 60}}
              showsVerticalScrollIndicator={false}
              showsHorizontalScrollIndicator={false}>
              {Zone.getSubZoneList(this.state.selectedMainZone).map(value => (
                <MatchingZoneViewSubZone
                  subZoneInfo={value}
                  selectedSubZone={this.state.selectedSubZone}
                  setSubZone={(sub: string) =>
                    this.setState({selectedSubZone: sub})
                  }
                />
              ))}
            </ScrollView>
          </View>
          <View style={this.styles.bottomWrapper}>
            <ScrollView
              horizontal={true}
              style={this.styles.scrollView}
              showsVerticalScrollIndicator={false}
              showsHorizontalScrollIndicator={false}>
              <MatchingZoneViewSmallZone
                selectedSubZone={this.state.selectedSubZone}
              />
            </ScrollView>
          </View>
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    innerModalConatiner: {
      flexDirection: 'row',
      marginTop: 20,
    },
    container: {width: '100%', flex: 1, backgroundColor: '#FFF'},
    mainZoneWrapper: {
      width: '50%',
      paddingRight: 20,
      borderRightWidth: 1,
      borderColor: '#f5f5f7',
    },
    bottomWrapper: {
      position: 'absolute',
      bottom: 0,
      backgroundColor: '#FFF',
      width: '120%',
    },
    scrollView: {
      paddingVertical: 10,
      height: 48,
      borderTopWidth: 1,
      borderTopColor: '#f5f5f7',
    },
    buttonText: {
      fontSize: 12,
      fontFamily: 'NotoSansCJKkr-Bold',
      lineHeight: 14,
      color: '#fff',
    },
    textWrapper: {
      height: 28,
      backgroundColor: '#f5f7ff',
      alignItems: 'center',
      justifyContent: 'center',
      paddingHorizontal: 10,
      borderRadius: 6,
      marginLeft: 5,
    },
  });
}
