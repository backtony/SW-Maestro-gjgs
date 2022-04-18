import React from 'react';
import {StyleSheet} from 'react-native';
import {View, Text} from 'react-native';
import {ScrollView} from 'react-native-gesture-handler';
import CurriculumBlock from './CurriculumBlock';

export class CurriculumInfo {
  curriculumId: number | undefined;
  order: number | undefined;
  title: string | undefined;
  detailText: string | undefined;
  curriculumImageUrl: string | undefined;
}

interface CurriculumTabProps {
  curriculumList: CurriculumInfo[];
}

export default class CurriculumTab extends React.Component<
  CurriculumTabProps,
  {}
> {
  render() {
    return (
      <ScrollView style={this.styles.scrollView}>
        <View style={this.styles.wrapper}>
          <Text style={this.styles.text}>커리큘럼</Text>
        </View>
        {this.props.curriculumList &&
          this.props.curriculumList.map((curriculum: CurriculumInfo) => (
            <CurriculumBlock curriculum={curriculum} />
          ))}
      </ScrollView>
    );
  }

  private styles = StyleSheet.create({
    scrollView: {flex: 1, backgroundColor: 'white'},
    wrapper: {
      flexDirection: 'row',
      marginTop: 20,
      alignItems: 'center',
      marginBottom: 20,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
  });
}
