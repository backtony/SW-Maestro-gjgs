import React from 'react';
import {StyleSheet} from 'react-native';
import {Image, Text, View} from 'react-native';
import {CurriculumInfo} from './CurriculumTab';

interface CurriculumBlockProps {
  curriculum: CurriculumInfo;
}

export default class CurriculumBlock extends React.Component<
  CurriculumBlockProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Text style={this.styles.title}>{this.props.curriculum.title}</Text>
        <Image
          style={this.styles.image}
          source={{
            uri: this.props.curriculum.curriculumImageUrl,
          }}
        />
        <Text style={this.styles.text}>{this.props.curriculum.detailText}</Text>
      </View>
    );
  }
  private styles = StyleSheet.create({
    container: {marginBottom: 40},
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 7,
      marginLeft: 6,
    },
    text: {
      fontSize: 12,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 7,
      marginLeft: 6,
    },
    image: {height: 200, borderRadius: 8, marginVertical: 10},
  });
}
