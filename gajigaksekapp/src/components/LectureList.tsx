import LectureBlock from '@/components/LectureBlock';
import React from 'react';
import {FlatList, StyleSheet, View} from 'react-native';

interface LectureListProps {
  data: any;
  navigation: any;
  refreshParent: () => void;
  deleteLecture: (lectureId: number) => void;
}

interface LectureListStates {
  refreshing: boolean;
}

export default class LectureList extends React.Component<
  LectureListProps,
  LectureListStates
> {
  constructor() {
    super();
    this.state = {refreshing: false};
  }
  render() {
    return (
      <FlatList
        style={this.styles.list}
        contentContainerStyle={this.styles.listContainer}
        data={this.props.data}
        numColumns={2}
        keyExtractor={item => {
          return item.id;
        }}
        ItemSeparatorComponent={() => {
          return <View style={this.styles.separator} />;
        }}
        renderItem={post => {
          const item = post.item;
          return (
            <LectureBlock
              navigation={this.props.navigation}
              refreshParent={() => {
                this.props.refreshParent();
              }}
              lecture={item}
              deleteLecture={(lectureId: number) =>
                this.props.deleteLecture(lectureId)
              }
            />
          );
        }}
        refreshing={this.state.refreshing}
        onRefresh={() => this.props.refreshParent()}
      />
    );
  }
  private styles = StyleSheet.create({
    list: {
      paddingHorizontal: 10,
    },
    listContainer: {
      marginHorizontal: 10,
      marginTop: 10,
    },
    separator: {
      marginTop: 10,
    },
  });
}
